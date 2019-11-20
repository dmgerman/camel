begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.amqp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|amqp
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|jms
operator|.
name|JmsConfiguration
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AMQPConfiguration
specifier|public
class|class
name|AMQPConfiguration
extends|extends
name|JmsConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Whether to include AMQP annotations when mapping from AMQP to Camel Message."
operator|+
literal|" Setting this to true will map AMQP message annotations to message headers."
operator|+
literal|" Due to limitations in Apache Qpid JMS API, currently delivery annotations are ignored."
argument_list|)
DECL|field|includeAmqpAnnotations
specifier|private
name|boolean
name|includeAmqpAnnotations
decl_stmt|;
DECL|method|AMQPConfiguration ()
specifier|public
name|AMQPConfiguration
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|AMQPConfiguration (ConnectionFactory connectionFactory)
specifier|public
name|AMQPConfiguration
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|super
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|isIncludeAmqpAnnotations ()
specifier|public
name|boolean
name|isIncludeAmqpAnnotations
parameter_list|()
block|{
return|return
name|includeAmqpAnnotations
return|;
block|}
comment|/**      * Whether to include AMQP annotations when mapping from AMQP to Camel Message.      * Setting this to true will map AMQP message annotations to message headers.      * Due to limitations in Apache Qpid JMS API, currently delivery annotations      * are ignored.      */
DECL|method|setIncludeAmqpAnnotations (boolean includeAmqpAnnotations)
specifier|public
name|void
name|setIncludeAmqpAnnotations
parameter_list|(
name|boolean
name|includeAmqpAnnotations
parameter_list|)
block|{
name|this
operator|.
name|includeAmqpAnnotations
operator|=
name|includeAmqpAnnotations
expr_stmt|;
block|}
block|}
end_class

end_unit

