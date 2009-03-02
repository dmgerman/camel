begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|NoTypeConversionAvailableException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|StreamCache
import|;
end_import

begin_comment
comment|/**  * Some helper methods when working with {@link org.apache.camel.Message}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MessageHelper
specifier|public
specifier|final
class|class
name|MessageHelper
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|MessageHelper ()
specifier|private
name|MessageHelper
parameter_list|()
block|{     }
comment|/**      * Extracts the given body and returns it as a String, that      * can be used for logging etc.      *<p/>      * Will handle stream based bodies wrapped in StreamCache.      *      * @param message  the message with the body      * @return the body as String, can return<tt>null</null> if no body      */
DECL|method|extractBodyAsString (Message message)
specifier|public
specifier|static
name|String
name|extractBodyAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StreamCache
name|newBody
init|=
literal|null
decl_stmt|;
try|try
block|{
name|newBody
operator|=
name|message
operator|.
name|getBody
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
comment|// ignore, in not of StreamCache type
block|}
name|Object
name|answer
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|ex
parameter_list|)
block|{
name|answer
operator|=
name|message
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
comment|// Reset the InputStreamCache
name|newBody
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
operator|!=
literal|null
condition|?
name|answer
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Gets the given body class type name as a String.      *<p/>      * Will skip java.lang. for the build in Java types.      *      * @param message  the message with the body      * @return the body typename as String, can return<tt>null</null> if no body      */
DECL|method|getBodyTypeName (Message message)
specifier|public
specifier|static
name|String
name|getBodyTypeName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|answer
init|=
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
operator|.
name|startsWith
argument_list|(
literal|"java.lang."
argument_list|)
condition|)
block|{
return|return
name|answer
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
return|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * If the message body contains a {@link StreamCache} instance, reset the cache to       * enable reading from it again.      *       * @param message the message for which to reset the body      */
DECL|method|resetStreamCache (Message message)
specifier|public
specifier|static
name|void
name|resetStreamCache
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
condition|)
block|{
operator|(
operator|(
name|StreamCache
operator|)
name|message
operator|.
name|getBody
argument_list|()
operator|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

