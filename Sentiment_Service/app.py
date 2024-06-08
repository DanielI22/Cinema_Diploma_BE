from flask import Flask, request, jsonify
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

app = Flask(__name__)
analyzer = SentimentIntensityAnalyzer()

def detect_emotion(compound_score):
    if compound_score >= 0.6:
        return 'happy'
    elif compound_score >= 0.05:
        return 'positive'
    elif compound_score <= -0.6:
        return 'angry'
    elif compound_score <= -0.05:
        return 'negative'
    else:
        return 'neutral'

@app.route('/analyze', methods=['POST'])
def analyze_sentiment():
    data = request.json
    text = data.get('text', '')
    sentiment_score = analyzer.polarity_scores(text)
    compound_score = sentiment_score['compound']
    emotion = detect_emotion(compound_score)
    return jsonify({'sentiment': emotion})

if __name__ == '__main__':
    app.run(port=5000, debug=True)
