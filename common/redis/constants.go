package redis

const GET_ALL string = "HGETALL"
const SET string = "HMSET"
const FLUSH_ALL string = "FLUSHALL"
const FLUSH string = "FLUSHDB"
const ADD_TO_SET string = "SADD"
const KEYS string = "KEYS"

const LIVE_MATCH string = "LiveMatch"
const SCORES_UPDATED string = "ScoresUpdated"
const ID = "id"
const FORMATTED_DATE = "formattedDate"
const HOME_TEAM = "hTeam"
const AWAY_TEAM = "aTeam"
const LAST_UPDATED = "lastUpdated"

const CLASS = "_class"
const LIVE_MATCH_CLASS = "com.cshep4.premierpredictor.matchupdate.entity.LiveMatchEntity"
const SCORES_UPDATED_CLASS = "com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity"

const SCORES_UPDATED_ID = "1"