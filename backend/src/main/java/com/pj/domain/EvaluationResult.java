package com.pj.domain;

public class EvaluationResult {
    private boolean correct;
    private String message;
    private int diffCount;

    public EvaluationResult() {
    }

    public EvaluationResult(boolean correct, String message, int diffCount) {
        this.correct = correct;
        this.message = message;
        this.diffCount = diffCount;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDiffCount() {
        return diffCount;
    }

    public void setDiffCount(int diffCount) {
        this.diffCount = diffCount;
    }
}
