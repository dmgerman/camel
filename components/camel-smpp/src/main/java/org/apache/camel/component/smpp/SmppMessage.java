begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|CamelContext
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
name|support
operator|.
name|DefaultMessage
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
name|support
operator|.
name|ExchangeHelper
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
name|jsmpp
operator|.
name|bean
operator|.
name|AlertNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|Alphabet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DataSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DeliverSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|MessageRequest
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
comment|/**  * Represents a {@link org.apache.camel.Message} for working with SMPP  */
end_comment

begin_class
DECL|class|SmppMessage
specifier|public
class|class
name|SmppMessage
extends|extends
name|DefaultMessage
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
name|SmppMessage
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|command
specifier|private
name|Command
name|command
decl_stmt|;
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|method|SmppMessage (CamelContext camelContext, Command command, SmppConfiguration configuration)
specifier|public
name|SmppMessage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Command
name|command
parameter_list|,
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|SmppMessage
name|newInstance
parameter_list|()
block|{
name|SmppMessage
name|answer
init|=
operator|new
name|SmppMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|null
argument_list|,
name|this
operator|.
name|configuration
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|isAlertNotification ()
specifier|public
name|boolean
name|isAlertNotification
parameter_list|()
block|{
return|return
name|command
operator|instanceof
name|AlertNotification
return|;
block|}
DECL|method|isDataSm ()
specifier|public
name|boolean
name|isDataSm
parameter_list|()
block|{
return|return
name|command
operator|instanceof
name|DataSm
return|;
block|}
DECL|method|isDeliverSm ()
specifier|public
name|boolean
name|isDeliverSm
parameter_list|()
block|{
return|return
name|command
operator|instanceof
name|DeliverSm
operator|&&
operator|!
operator|(
operator|(
name|DeliverSm
operator|)
name|command
operator|)
operator|.
name|isSmscDeliveryReceipt
argument_list|()
return|;
block|}
DECL|method|isDeliveryReceipt ()
specifier|public
name|boolean
name|isDeliveryReceipt
parameter_list|()
block|{
return|return
name|command
operator|instanceof
name|DeliverSm
operator|&&
operator|(
operator|(
name|DeliverSm
operator|)
name|command
operator|)
operator|.
name|isSmscDeliveryReceipt
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|command
operator|instanceof
name|MessageRequest
condition|)
block|{
name|MessageRequest
name|msgRequest
init|=
operator|(
name|MessageRequest
operator|)
name|command
decl_stmt|;
name|byte
index|[]
name|shortMessage
init|=
name|msgRequest
operator|.
name|getShortMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|shortMessage
operator|==
literal|null
operator|||
name|shortMessage
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Alphabet
name|alphabet
init|=
name|Alphabet
operator|.
name|parseDataCoding
argument_list|(
name|msgRequest
operator|.
name|getDataCoding
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|SmppUtils
operator|.
name|is8Bit
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
return|return
name|shortMessage
return|;
block|}
name|String
name|encoding
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|getExchange
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|encoding
argument_list|)
operator|||
operator|!
name|Charset
operator|.
name|isSupported
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|configuration
operator|.
name|getEncoding
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|shortMessage
argument_list|,
name|encoding
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsupported encoding \"{}\". Using system default encoding."
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|shortMessage
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|command
operator|!=
literal|null
condition|)
block|{
return|return
literal|"SmppMessage: "
operator|+
name|command
return|;
block|}
else|else
block|{
return|return
literal|"SmppMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the underlying jSMPP command      *       * @return command      */
DECL|method|getCommand ()
specifier|public
name|Command
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
block|}
end_class

end_unit

