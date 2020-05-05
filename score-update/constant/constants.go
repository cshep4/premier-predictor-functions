package constant

import "errors"

var ErrScoresAlreadyUpdated = errors.New("scores have already been updated today")
var ErrScoresDoNotNeedUpdating = errors.New("scores don't need updating")
var ErrUpdatingScores = errors.New("error updating scores")

const SCORE_UPDATE_URL = "https://jbemuyb0o3.execute-api.us-east-1.amazonaws.com/dev/user/score"
const EMAIL_ADDRESS = "shepapps4@gmail.com"
const SENDER = "Score Updater"
