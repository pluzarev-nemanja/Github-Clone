package com.example.github.pullRequest.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.github.pullRequest.model.LabelUiModel
import com.example.github.pullRequest.model.PullRequestDetailsUiModel
import com.example.github.pullRequest.model.ReviewerUiModel

class PullRequestDetailsProvider : PreviewParameterProvider<PullRequestDetailsUiModel> {
    override val values: Sequence<PullRequestDetailsUiModel>
        get() =
            sequenceOf(
                PullRequestDetailsUiModel(
                    authorName = "nikTC",
                    title = "Timeout error fixed",
                    labels =
                        listOf(
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                            LabelUiModel(
                                labelColor = "",
                                labelName = "label name",
                            ),
                        ),
                    reviewers =
                        listOf(
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                            ReviewerUiModel(
                                reviewerImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                                reviewerName = "Reviewer Name",
                            ),
                        ),
                    milestone = "100 pull request closed",
                    description = "Description text",
                    authorImage = "https://avatars.githubusercontent.com/u/34889?v=4",
                ),
            )
}
