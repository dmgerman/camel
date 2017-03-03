begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.box2.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|box2
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxAPIException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxComment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|box
operator|.
name|sdk
operator|.
name|BoxFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Box2 Comments Manager  *   *<p>  * Provides operations to manage Box comments.  *   *   *  */
end_comment

begin_class
DECL|class|Box2CommentsManager
specifier|public
class|class
name|Box2CommentsManager
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Box2CommentsManager
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Box connection to authenticated user account.      */
DECL|field|boxConnection
specifier|private
name|BoxAPIConnection
name|boxConnection
decl_stmt|;
comment|/**      * Create comments manager to manage the comments of Box connection's      * authenticated user.      *       * @param boxConnection      *            - Box connection to authenticated user account.      */
DECL|method|Box2CommentsManager (BoxAPIConnection boxConnection)
specifier|public
name|Box2CommentsManager
parameter_list|(
name|BoxAPIConnection
name|boxConnection
parameter_list|)
block|{
name|this
operator|.
name|boxConnection
operator|=
name|boxConnection
expr_stmt|;
block|}
comment|/**      * Add comment to file.      *       * @param fileId      *            - the id of file to rename.      * @param message      *            - the comment's message.      * @return The commented file.      */
DECL|method|addFileComment (String fileId, String message)
specifier|public
name|BoxFile
name|addFileComment
parameter_list|(
name|String
name|fileId
parameter_list|,
name|String
name|message
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Adding comment to file(id="
operator|+
name|fileId
operator|+
literal|") to '"
operator|+
name|message
operator|+
literal|"'"
argument_list|)
expr_stmt|;
if|if
condition|(
name|fileId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'fileId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'message' can not be null"
argument_list|)
throw|;
block|}
name|BoxFile
name|fileToCommentOn
init|=
operator|new
name|BoxFile
argument_list|(
name|boxConnection
argument_list|,
name|fileId
argument_list|)
decl_stmt|;
name|fileToCommentOn
operator|.
name|addComment
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|fileToCommentOn
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get a list of any comments on this file.      *       * @param fileId      *            - the id of file.      * @return The list of comments on this file.      */
DECL|method|getFileComments (String fileId)
specifier|public
name|List
argument_list|<
name|BoxComment
operator|.
name|Info
argument_list|>
name|getFileComments
parameter_list|(
name|String
name|fileId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting comments of file(id="
operator|+
name|fileId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|fileId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'fileId' can not be null"
argument_list|)
throw|;
block|}
name|BoxFile
name|file
init|=
operator|new
name|BoxFile
argument_list|(
name|boxConnection
argument_list|,
name|fileId
argument_list|)
decl_stmt|;
return|return
name|file
operator|.
name|getComments
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Get comment information.      *       * @param commentId      *            - the id of comment.      * @return The comment information.      */
DECL|method|getCommentInfo (String commentId)
specifier|public
name|BoxComment
operator|.
name|Info
name|getCommentInfo
parameter_list|(
name|String
name|commentId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting info for comment(id="
operator|+
name|commentId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|commentId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'commentId' can not be null"
argument_list|)
throw|;
block|}
name|BoxComment
name|comment
init|=
operator|new
name|BoxComment
argument_list|(
name|boxConnection
argument_list|,
name|commentId
argument_list|)
decl_stmt|;
return|return
name|comment
operator|.
name|getInfo
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Reply to a comment.      *       * @param commentId      *            - the id of comment to reply to.      * @param message      *            - the message for the reply.      * @return The newly created reply comment.      */
DECL|method|replyToComment (String commentId, String message)
specifier|public
name|BoxComment
name|replyToComment
parameter_list|(
name|String
name|commentId
parameter_list|,
name|String
name|message
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Replying to comment(id="
operator|+
name|commentId
operator|+
literal|") with message="
operator|+
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|commentId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'commentId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'message' can not be null"
argument_list|)
throw|;
block|}
name|BoxComment
name|comment
init|=
operator|new
name|BoxComment
argument_list|(
name|boxConnection
argument_list|,
name|commentId
argument_list|)
decl_stmt|;
return|return
name|comment
operator|.
name|reply
argument_list|(
name|message
argument_list|)
operator|.
name|getResource
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Change comment message.      *       * @param commentId      *            - the id of comment to change.      * @param message      *            - the new message for the comment.      * @return The comment with changed message.      */
DECL|method|changeCommentMessage (String commentId, String message)
specifier|public
name|BoxComment
name|changeCommentMessage
parameter_list|(
name|String
name|commentId
parameter_list|,
name|String
name|message
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Changing comment(id="
operator|+
name|commentId
operator|+
literal|") message="
operator|+
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|commentId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'commentId' can not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'message' can not be null"
argument_list|)
throw|;
block|}
name|BoxComment
name|comment
init|=
operator|new
name|BoxComment
argument_list|(
name|boxConnection
argument_list|,
name|commentId
argument_list|)
decl_stmt|;
return|return
name|comment
operator|.
name|changeMessage
argument_list|(
name|message
argument_list|)
operator|.
name|getResource
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Delete comment.      *       * @param commentId      *            - the id of comment to delete.      */
DECL|method|deleteComment (String commentId)
specifier|public
name|void
name|deleteComment
parameter_list|(
name|String
name|commentId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Deleting comment(id="
operator|+
name|commentId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|commentId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Parameter 'commentId' can not be null"
argument_list|)
throw|;
block|}
name|BoxComment
name|comment
init|=
operator|new
name|BoxComment
argument_list|(
name|boxConnection
argument_list|,
name|commentId
argument_list|)
decl_stmt|;
name|comment
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BoxAPIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Box API returned the error code %d\n\n%s"
argument_list|,
name|e
operator|.
name|getResponseCode
argument_list|()
argument_list|,
name|e
operator|.
name|getResponse
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

