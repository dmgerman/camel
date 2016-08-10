begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
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
name|component
operator|.
name|avro
operator|.
name|AvroConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Working with Apache Avro for data serialization.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.avro"
argument_list|)
DECL|class|AvroComponentConfiguration
specifier|public
class|class
name|AvroComponentConfiguration
block|{
comment|/**      * To use a shared AvroConfiguration to configure options once. Properties      * of the shared configuration can also be set individually.      */
DECL|field|configuration
specifier|private
name|AvroConfiguration
name|configuration
decl_stmt|;
comment|/**      * Hostname to use      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * Port number to use      */
DECL|field|port
specifier|private
name|Integer
name|port
decl_stmt|;
comment|/**      * Avro protocol to use      */
DECL|field|protocol
specifier|private
name|Protocol
name|protocol
decl_stmt|;
comment|/**      * Transport to use      */
DECL|field|transport
specifier|private
name|String
name|transport
decl_stmt|;
comment|/**      * Avro protocol location      */
DECL|field|protocolLocation
specifier|private
name|String
name|protocolLocation
decl_stmt|;
comment|/**      * Avro protocol to use defined by the FQN class name      */
DECL|field|protocolClassName
specifier|private
name|String
name|protocolClassName
decl_stmt|;
comment|/**      * The name of the message to send.      */
DECL|field|messageName
specifier|private
name|String
name|messageName
decl_stmt|;
comment|/**      * Authority to use (username and password)      */
DECL|field|uriAuthority
specifier|private
name|String
name|uriAuthority
decl_stmt|;
comment|/**      * If protocol object provided is reflection protocol. Should be used only      * with protocol parameter because for protocolClassName protocol type will      * be auto detected      */
DECL|field|reflectionProtocol
specifier|private
name|Boolean
name|reflectionProtocol
decl_stmt|;
comment|/**      * If true consumer parameter won't be wrapped into array. Will fail if      * protocol specifies more then 1 parameter for the message      */
DECL|field|singleParameter
specifier|private
name|Boolean
name|singleParameter
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|AvroConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (AvroConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AvroConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getProtocol ()
specifier|public
name|Protocol
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
DECL|method|setProtocol (Protocol protocol)
specifier|public
name|void
name|setProtocol
parameter_list|(
name|Protocol
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
DECL|method|getTransport ()
specifier|public
name|String
name|getTransport
parameter_list|()
block|{
return|return
name|transport
return|;
block|}
DECL|method|setTransport (String transport)
specifier|public
name|void
name|setTransport
parameter_list|(
name|String
name|transport
parameter_list|)
block|{
name|this
operator|.
name|transport
operator|=
name|transport
expr_stmt|;
block|}
DECL|method|getProtocolLocation ()
specifier|public
name|String
name|getProtocolLocation
parameter_list|()
block|{
return|return
name|protocolLocation
return|;
block|}
DECL|method|setProtocolLocation (String protocolLocation)
specifier|public
name|void
name|setProtocolLocation
parameter_list|(
name|String
name|protocolLocation
parameter_list|)
block|{
name|this
operator|.
name|protocolLocation
operator|=
name|protocolLocation
expr_stmt|;
block|}
DECL|method|getProtocolClassName ()
specifier|public
name|String
name|getProtocolClassName
parameter_list|()
block|{
return|return
name|protocolClassName
return|;
block|}
DECL|method|setProtocolClassName (String protocolClassName)
specifier|public
name|void
name|setProtocolClassName
parameter_list|(
name|String
name|protocolClassName
parameter_list|)
block|{
name|this
operator|.
name|protocolClassName
operator|=
name|protocolClassName
expr_stmt|;
block|}
DECL|method|getMessageName ()
specifier|public
name|String
name|getMessageName
parameter_list|()
block|{
return|return
name|messageName
return|;
block|}
DECL|method|setMessageName (String messageName)
specifier|public
name|void
name|setMessageName
parameter_list|(
name|String
name|messageName
parameter_list|)
block|{
name|this
operator|.
name|messageName
operator|=
name|messageName
expr_stmt|;
block|}
DECL|method|getUriAuthority ()
specifier|public
name|String
name|getUriAuthority
parameter_list|()
block|{
return|return
name|uriAuthority
return|;
block|}
DECL|method|setUriAuthority (String uriAuthority)
specifier|public
name|void
name|setUriAuthority
parameter_list|(
name|String
name|uriAuthority
parameter_list|)
block|{
name|this
operator|.
name|uriAuthority
operator|=
name|uriAuthority
expr_stmt|;
block|}
DECL|method|getReflectionProtocol ()
specifier|public
name|Boolean
name|getReflectionProtocol
parameter_list|()
block|{
return|return
name|reflectionProtocol
return|;
block|}
DECL|method|setReflectionProtocol (Boolean reflectionProtocol)
specifier|public
name|void
name|setReflectionProtocol
parameter_list|(
name|Boolean
name|reflectionProtocol
parameter_list|)
block|{
name|this
operator|.
name|reflectionProtocol
operator|=
name|reflectionProtocol
expr_stmt|;
block|}
DECL|method|getSingleParameter ()
specifier|public
name|Boolean
name|getSingleParameter
parameter_list|()
block|{
return|return
name|singleParameter
return|;
block|}
DECL|method|setSingleParameter (Boolean singleParameter)
specifier|public
name|void
name|setSingleParameter
parameter_list|(
name|Boolean
name|singleParameter
parameter_list|)
block|{
name|this
operator|.
name|singleParameter
operator|=
name|singleParameter
expr_stmt|;
block|}
block|}
end_class

end_unit

