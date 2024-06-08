package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.user.add.AddUserOperation;
import sit.tu_varna.bg.api.operation.user.add.AddUserRequest;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordOperation;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordRequest;
import sit.tu_varna.bg.api.operation.user.changeusername.ChangeUsernameOperation;
import sit.tu_varna.bg.api.operation.user.changeusername.ChangeUsernameRequest;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserOperation;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserRequest;
import sit.tu_varna.bg.api.operation.user.forgotpassword.ForgotPasswordOperation;
import sit.tu_varna.bg.api.operation.user.forgotpassword.ForgotPasswordRequest;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersOperation;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersRequest;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.logout.LogoutOperation;
import sit.tu_varna.bg.api.operation.user.logout.LogoutRequest;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshOperation;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.resend.ResendVerificationOperation;
import sit.tu_varna.bg.api.operation.user.resend.ResendVerificationRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.ADMIN_ROLE;
import static sit.tu_varna.bg.core.constants.BusinessConstants.USER_ID_CLAIM;

@Path("/api/users")
@Authenticated
public class UserResource {
    @Inject
    GetUsersOperation getUsersOperation;
    @Inject
    AddUserOperation addUserOperation;
    @Inject
    ChangeUsernameOperation changeUsernameOperation;
    @Inject
    ChangePasswordOperation changePasswordOperation;
    @Inject
    DeleteUserOperation deleteUserOperation;
    @Inject
    RegisterOperation registerOperation;
    @Inject
    LoginOperation loginOperation;
    @Inject
    LogoutOperation logoutOperation;
    @Inject
    RefreshOperation refreshOperation;
    @Inject
    ResendVerificationOperation resendVerificationOperation;
    @Inject
    ForgotPasswordOperation forgotPasswordOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getAllUsers() {
        return Response.ok(getUsersOperation.process(new GetUsersRequest())).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response addUser(@Valid AddUserRequest request) {
        return Response.ok(addUserOperation.process(request)).build();
    }

    @PUT
    @Path("/change-username")
    public Response changeUsername(@Valid ChangeUsernameRequest request) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        request.setUserId(UUID.fromString(userId));
        return Response.ok(changeUsernameOperation.process(request)).build();
    }

    @PUT
    @Path("/change-password")
    public Response changePassword(@Valid ChangePasswordRequest request) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        request.setUserId(UUID.fromString(userId));
        return Response.ok(changePasswordOperation.process(request)).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId")
                               @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                       message = "Invalid UUID format")
                                       String userId) {
        DeleteUserRequest deleteUserRequest = DeleteUserRequest
                .builder()
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(deleteUserOperation.process(deleteUserRequest)).build();
    }

    @POST
    @PermitAll
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        return Response.ok(registerOperation.process(request)).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        return Response.ok(loginOperation.process(request)).build();
    }

    @POST
    @PermitAll
    @Path("/logout")
    public Response logout(@Valid LogoutRequest request) {
        return Response.ok(logoutOperation.process(request)).build();
    }

    @POST
    @PermitAll
    @Path("/refresh")
    public Response refresh(@Valid RefreshRequest request) {
        return Response.ok(refreshOperation.process(request)).build();
    }

    @POST
    @PermitAll
    @Path("/forgot-password")
    public Response forgotPassword(@Valid ForgotPasswordRequest request) {
        return Response.ok(forgotPasswordOperation.process(request)).build();
    }

    @POST
    @PermitAll
    @Path("/resend-verification")
    public Response resendVerification(@Valid ResendVerificationRequest request) {
        return Response.ok(resendVerificationOperation.process(request)).build();
    }
}
