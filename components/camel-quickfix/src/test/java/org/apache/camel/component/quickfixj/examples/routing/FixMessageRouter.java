begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.examples.routing
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
operator|.
name|examples
operator|.
name|routing
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
name|Exchange
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
name|util
operator|.
name|ObjectHelper
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
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
operator|.
name|Header
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
name|BeginString
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|DeliverToCompID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|DeliverToLocationID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|DeliverToSubID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|OnBehalfOfCompID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|OnBehalfOfLocationID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|OnBehalfOfSubID
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
name|SenderLocationID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|SenderSubID
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

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|TargetLocationID
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|field
operator|.
name|TargetSubID
import|;
end_import

begin_comment
comment|/**  * Routes exchanges based on FIX-specific routing fields in the message.  */
end_comment

begin_class
DECL|class|FixMessageRouter
specifier|public
class|class
name|FixMessageRouter
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
name|FixMessageRouter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|engineUri
specifier|private
specifier|final
name|String
name|engineUri
decl_stmt|;
DECL|method|FixMessageRouter (String engineUri)
specifier|public
name|FixMessageRouter
parameter_list|(
name|String
name|engineUri
parameter_list|)
block|{
name|this
operator|.
name|engineUri
operator|=
name|engineUri
expr_stmt|;
block|}
DECL|method|route (Exchange exchange)
specifier|public
name|String
name|route
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|SessionID
name|destinationSession
init|=
name|getDestinationSessionID
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinationSession
operator|!=
literal|null
condition|)
block|{
name|String
name|destinationUri
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s?sessionID=%s"
argument_list|,
name|engineUri
argument_list|,
name|destinationSession
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Routing destination: {}"
argument_list|,
name|destinationUri
argument_list|)
expr_stmt|;
return|return
name|destinationUri
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getDestinationSessionID (Message message)
specifier|private
name|SessionID
name|getDestinationSessionID
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Header
name|header
init|=
name|message
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|String
name|fixVersion
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|BeginString
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|String
name|destinationCompId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|DeliverToCompID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
if|if
condition|(
name|destinationCompId
operator|!=
literal|null
condition|)
block|{
name|String
name|destinationSubId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|DeliverToSubID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|String
name|destinationLocationId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|DeliverToLocationID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|header
operator|.
name|removeField
argument_list|(
name|DeliverToCompID
operator|.
name|FIELD
argument_list|)
expr_stmt|;
name|header
operator|.
name|removeField
argument_list|(
name|DeliverToSubID
operator|.
name|FIELD
argument_list|)
expr_stmt|;
name|header
operator|.
name|removeField
argument_list|(
name|DeliverToLocationID
operator|.
name|FIELD
argument_list|)
expr_stmt|;
name|String
name|gatewayCompId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|TargetCompID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|String
name|gatewaySubId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|TargetSubID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|String
name|gatewayLocationId
init|=
name|getField
argument_list|(
name|header
argument_list|,
name|TargetLocationID
operator|.
name|FIELD
argument_list|)
decl_stmt|;
name|header
operator|.
name|setString
argument_list|(
name|OnBehalfOfCompID
operator|.
name|FIELD
argument_list|,
name|getField
argument_list|(
name|header
argument_list|,
name|SenderCompID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|header
operator|.
name|isSetField
argument_list|(
name|SenderSubID
operator|.
name|FIELD
argument_list|)
condition|)
block|{
name|header
operator|.
name|setString
argument_list|(
name|OnBehalfOfSubID
operator|.
name|FIELD
argument_list|,
name|getField
argument_list|(
name|header
argument_list|,
name|SenderSubID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|.
name|isSetField
argument_list|(
name|SenderLocationID
operator|.
name|FIELD
argument_list|)
condition|)
block|{
name|header
operator|.
name|setString
argument_list|(
name|OnBehalfOfLocationID
operator|.
name|FIELD
argument_list|,
name|getField
argument_list|(
name|header
argument_list|,
name|SenderLocationID
operator|.
name|FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SessionID
argument_list|(
name|fixVersion
argument_list|,
name|gatewayCompId
argument_list|,
name|gatewaySubId
argument_list|,
name|gatewayLocationId
argument_list|,
name|destinationCompId
argument_list|,
name|destinationSubId
argument_list|,
name|destinationLocationId
argument_list|,
literal|null
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getField (FieldMap fieldMap, int tag)
specifier|private
name|String
name|getField
parameter_list|(
name|FieldMap
name|fieldMap
parameter_list|,
name|int
name|tag
parameter_list|)
block|{
if|if
condition|(
name|fieldMap
operator|.
name|isSetField
argument_list|(
name|tag
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|fieldMap
operator|.
name|getString
argument_list|(
name|tag
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

