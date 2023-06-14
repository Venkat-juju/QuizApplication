package com.example.quizapplication.quiz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.quizapplication.quiz.data.local.entities.QuestionsEntity
import com.example.quizapplication.quiz.data.local.entities.QuizHistoryEntity
import com.example.quizapplication.quiz.data.local.entities.TopicsEntity

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topic: List<TopicsEntity>)

    @Query("""
        SELECT * from topicsentity WHERE subjectName LIKE :subjectName
    """)
    suspend fun getTopicsBySubjectName(subjectName: String): List<TopicsEntity>

    @Update
    suspend fun bookmarkQuestion(questionsEntity: QuestionsEntity)

    @Query("""
        SELECT DISTINCT subjectName from topicsentity
    """)
    suspend fun getAllSubjects(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionsEntity>)

    @Query("""
         SELECT * from questionsentity WHERE topicId IN (:topicId) LIMIT :limit
    """)
    suspend fun getQuestionsByTopic(topicId: List<Long>, limit: Int): List<QuestionsEntity>


    @Query("""
        SELECT * FROM questionsentity ORDER BY RANDOM() LIMIT :numberOfQuestions    
    """)
    suspend fun getRandomQuestions(numberOfQuestions: Int): List<QuestionsEntity>

    @Query("""
        UPDATE questionsentity SET isAttempted = 1 WHERE questionId in (:questionsId)
    """)
    suspend fun markQuestionsAsAttempted(questionsId: List<Long>)

    @Query("""
        SELECT COUNT(isAttempted) FROM questionsentity WHERE topicId = :topicsId AND isAttempted = 1
    """)
    suspend fun getNumberOfQuestionsAttemptedByTopic(topicsId: Long): Int

    @Query("""
        SELECT * FROM quizhistoryentity
    """)
    suspend fun getAllHistory(): List<QuizHistoryEntity>

    @Query("""
        SELECT * FROM quizhistoryentity WHERE historyId = :historyId
    """)
    suspend fun getHistoryQuestions(historyId: Long): List<QuizHistoryEntity>

    @Query("""
        DELETE FROM quizhistoryentity
    """)
    suspend fun deleteAllHistory()

    @Query("""
        SELECT * FROM questionsentity WHERE questionId IN (:questionsId)
    """)
    suspend fun getQuestionsById(questionsId: List<Long>): List<QuestionsEntity>

    @Insert
    suspend fun insertHistory(questions: List<QuizHistoryEntity>)

    @Query("""
        SELECT historyId FROM quizhistoryentity ORDER BY historyId DESC LIMIT 1        
    """)
    suspend fun getLatestHistoryId(): Long?

    @Query("""
        SELECT * FROM questionsentity WHERE isBookmarked == 1
    """)
    suspend fun getAllBookmarkedQuestions(): List<QuestionsEntity>

    @Query("""
        SELECT * FROM topicsentity WHERE topicId IN (:ids)
    """)
    suspend fun getTopicsById(ids: List<Long>): List<TopicsEntity>

    @Query("""
        SELECT * FROM questionsentity WHERE topicId == :topicId AND isBookmarked = 1 
    """)
    suspend fun getBookmarksByTopic(topicId: Long): List<QuestionsEntity>
}