package com.envived.android.features.program;

public interface ProgramUpdateListener {
	public void onProgramInit(ProgramFeature initProgramFeature);
	public void onProgramUpdated(ProgramFeature updatedProgramFeature);
}
