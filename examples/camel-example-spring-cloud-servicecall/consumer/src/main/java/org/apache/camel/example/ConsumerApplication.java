begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|builder
operator|.
name|RouteBuilder
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
name|SpringApplication
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
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|discovery
operator|.
name|EnableDiscoveryClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_comment
comment|/**  * A sample Spring Boot application that starts the Camel routes.  */
end_comment

begin_class
annotation|@
name|SpringBootApplication
annotation|@
name|EnableDiscoveryClient
DECL|class|ConsumerApplication
specifier|public
class|class
name|ConsumerApplication
block|{
annotation|@
name|Component
DECL|class|ConsumerRoute
specifier|public
class|class
name|ConsumerRoute
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|rest
argument_list|(
literal|"/serviceCall"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/{serviceId}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:service-call"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:service-call"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|null
argument_list|)
operator|.
name|removeHeaders
argument_list|(
literal|"CamelHttp*"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:service-call?level=INFO&showAll=true&multiline=true"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"serviceId"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"service1"
argument_list|)
argument_list|)
operator|.
name|serviceCall
argument_list|(
literal|"service-1"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|log
argument_list|(
literal|"service-1 : ${body}"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"serviceId"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"service2"
argument_list|)
argument_list|)
operator|.
name|serviceCall
argument_list|(
literal|"service-2"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|log
argument_list|(
literal|"service-1 : ${body}"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A main method to start this application.      */
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|SpringApplication
operator|.
name|run
argument_list|(
name|ConsumerApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

