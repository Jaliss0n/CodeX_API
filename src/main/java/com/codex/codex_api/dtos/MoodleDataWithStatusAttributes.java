package com.codex.codex_api.dtos;

import com.codex.codex_api.external.api.MoodleData;
import lombok.Data;

@Data
public class MoodleDataWithStatusAttributes {

    private MoodleData moodleData;
    private boolean alreadyCadastred;

    public MoodleDataWithStatusAttributes (MoodleData moodleData, boolean alreadyCadastred) {
        this.moodleData = moodleData;
        this.alreadyCadastred = alreadyCadastred;
    }
}
