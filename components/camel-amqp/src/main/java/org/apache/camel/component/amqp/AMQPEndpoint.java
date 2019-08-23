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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncEndpoint
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
name|JmsEndpoint
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
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * Messaging with AMQP protocol using Apache QPid Client.  *  * This class extends JmsEndpoint because it uses Apache Qpid JMS-compatible client for  * performing the AMQP connectivity.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.2.0"
argument_list|,
name|scheme
operator|=
literal|"amqp"
argument_list|,
name|extendsScheme
operator|=
literal|"jms"
argument_list|,
name|title
operator|=
literal|"AMQP"
argument_list|,
name|syntax
operator|=
literal|"amqp:destinationType:destinationName"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|AMQPEndpoint
specifier|public
class|class
name|AMQPEndpoint
extends|extends
name|JmsEndpoint
implements|implements
name|AsyncEndpoint
block|{  }
end_class

end_unit

