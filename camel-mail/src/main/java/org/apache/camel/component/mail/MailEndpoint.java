begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|javamail
operator|.
name|JavaMailSender
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Folder
import|;
end_import

begin_comment
comment|/**  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MailEndpoint
specifier|public
class|class
name|MailEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|MailExchange
argument_list|>
block|{
DECL|field|binding
specifier|private
name|MailBinding
name|binding
decl_stmt|;
DECL|field|configuration
specifier|private
name|MailConfiguration
name|configuration
decl_stmt|;
DECL|method|MailEndpoint (String uri, MailComponent component, MailConfiguration configuration)
specifier|public
name|MailEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MailComponent
name|component
parameter_list|,
name|MailConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|MailExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaMailSender
name|sender
init|=
name|configuration
operator|.
name|createJavaMailConnection
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|createProducer
argument_list|(
name|sender
argument_list|)
return|;
block|}
comment|/**      * Creates a producer using the given sender      */
DECL|method|createProducer (JavaMailSender sender)
specifier|public
name|Producer
argument_list|<
name|MailExchange
argument_list|>
name|createProducer
parameter_list|(
name|JavaMailSender
name|sender
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|MailProducer
argument_list|(
name|this
argument_list|,
name|sender
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor<MailExchange> processor)
specifier|public
name|Consumer
argument_list|<
name|MailExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|MailExchange
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|JavaMailConnection
name|connection
init|=
name|configuration
operator|.
name|createJavaMailConnection
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|String
name|protocol
init|=
name|getConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
if|if
condition|(
name|protocol
operator|.
name|equals
argument_list|(
literal|"smtp"
argument_list|)
condition|)
block|{
name|protocol
operator|=
literal|"pop3"
expr_stmt|;
block|}
name|String
name|folderName
init|=
name|getConfiguration
argument_list|()
operator|.
name|getFolderName
argument_list|()
decl_stmt|;
name|Folder
name|folder
init|=
name|connection
operator|.
name|getFolder
argument_list|(
name|protocol
argument_list|,
name|folderName
argument_list|)
decl_stmt|;
if|if
condition|(
name|folder
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No folder for protocol: "
operator|+
name|protocol
operator|+
literal|" and name: "
operator|+
name|folderName
argument_list|)
throw|;
block|}
return|return
name|createConsumer
argument_list|(
name|processor
argument_list|,
name|folder
argument_list|)
return|;
block|}
comment|/**      * Creates a consumer using the given processor and transport      *      * @param processor the processor to use to process the messages      * @param folder the JavaMail Folder to use for inbound messages      * @return a newly created consumer      * @throws Exception if the consumer cannot be created      */
DECL|method|createConsumer (Processor<MailExchange> processor, Folder folder)
specifier|public
name|Consumer
argument_list|<
name|MailExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
argument_list|<
name|MailExchange
argument_list|>
name|processor
parameter_list|,
name|Folder
name|folder
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|MailConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|folder
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|MailExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|MailExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createExchange (Message message)
specifier|public
name|MailExchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|MailExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBinding ()
specifier|public
name|MailBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|MailBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a Mail message      *      * @param binding the binding to use      */
DECL|method|setBinding (MailBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|MailBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MailConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

