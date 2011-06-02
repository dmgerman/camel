begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|quickfix
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FieldMap
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|FieldNotFound
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|MsgType
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|SenderCompID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|TargetCompID
import|;
end_import

begin_class
DECL|class|MessagePredicate
specifier|public
class|class
name|MessagePredicate
block|{
DECL|field|headerCriteria
specifier|private
specifier|final
name|List
argument_list|<
name|Field
argument_list|<
name|String
argument_list|>
argument_list|>
name|headerCriteria
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|bodyCriteria
specifier|private
specifier|final
name|List
argument_list|<
name|Field
argument_list|<
name|String
argument_list|>
argument_list|>
name|bodyCriteria
init|=
operator|new
name|ArrayList
argument_list|<
name|Field
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|MessagePredicate (SessionID requestingSessionID, String msgType)
specifier|public
name|MessagePredicate
parameter_list|(
name|SessionID
name|requestingSessionID
parameter_list|,
name|String
name|msgType
parameter_list|)
block|{
comment|// Reverse session ID for reply
comment|// TODO may need to optionally include subID and locationID
name|addHeaderFieldIfPresent
argument_list|(
name|SenderCompID
operator|.
name|FIELD
argument_list|,
name|requestingSessionID
operator|.
name|getTargetCompID
argument_list|()
argument_list|)
expr_stmt|;
name|addHeaderFieldIfPresent
argument_list|(
name|TargetCompID
operator|.
name|FIELD
argument_list|,
name|requestingSessionID
operator|.
name|getSenderCompID
argument_list|()
argument_list|)
expr_stmt|;
name|withMessageType
argument_list|(
name|msgType
argument_list|)
expr_stmt|;
block|}
DECL|method|addHeaderFieldIfPresent (int tag, String value)
specifier|private
name|void
name|addHeaderFieldIfPresent
parameter_list|(
name|int
name|tag
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|withHeaderField
argument_list|(
name|tag
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|evaluate (Message message)
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|evaluate
argument_list|(
name|message
argument_list|,
name|bodyCriteria
argument_list|)
operator|&&
name|evaluate
argument_list|(
name|message
operator|.
name|getHeader
argument_list|()
argument_list|,
name|headerCriteria
argument_list|)
return|;
block|}
DECL|method|evaluate (FieldMap fieldMap, List<Field<String>> criteria)
specifier|private
name|boolean
name|evaluate
parameter_list|(
name|FieldMap
name|fieldMap
parameter_list|,
name|List
argument_list|<
name|Field
argument_list|<
name|String
argument_list|>
argument_list|>
name|criteria
parameter_list|)
block|{
for|for
control|(
name|Field
argument_list|<
name|String
argument_list|>
name|c
range|:
name|criteria
control|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|fieldMap
operator|.
name|isSetField
argument_list|(
name|c
operator|.
name|getField
argument_list|()
argument_list|)
condition|)
block|{
name|value
operator|=
name|fieldMap
operator|.
name|getString
argument_list|(
name|c
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FieldNotFound
name|e
parameter_list|)
block|{
comment|// ignored, shouldn't happen
block|}
if|if
condition|(
operator|!
name|c
operator|.
name|getObject
argument_list|()
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|withField (int tag, String value)
specifier|public
name|MessagePredicate
name|withField
parameter_list|(
name|int
name|tag
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|bodyCriteria
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|<
name|String
argument_list|>
argument_list|(
name|tag
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withHeaderField (int tag, String value)
specifier|public
name|MessagePredicate
name|withHeaderField
parameter_list|(
name|int
name|tag
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|headerCriteria
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|<
name|String
argument_list|>
argument_list|(
name|tag
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withMessageType (String msgType)
specifier|private
name|MessagePredicate
name|withMessageType
parameter_list|(
name|String
name|msgType
parameter_list|)
block|{
name|headerCriteria
operator|.
name|add
argument_list|(
operator|new
name|Field
argument_list|<
name|String
argument_list|>
argument_list|(
name|MsgType
operator|.
name|FIELD
argument_list|,
name|msgType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

