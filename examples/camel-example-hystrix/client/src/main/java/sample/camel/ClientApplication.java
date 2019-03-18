begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
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
name|component
operator|.
name|hystrix
operator|.
name|metrics
operator|.
name|servlet
operator|.
name|HystrixEventStreamServlet
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
name|boot
operator|.
name|web
operator|.
name|servlet
operator|.
name|ServletRegistrationBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_comment
comment|/**  * A Spring Boot application that runs the Camel Hystrix client application that calls service 1 and service 2 (as fallback)  */
end_comment

begin_class
annotation|@
name|SpringBootApplication
DECL|class|ClientApplication
specifier|public
class|class
name|ClientApplication
block|{
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
name|ClientApplication
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
DECL|method|hystrixServlet ()
specifier|public
name|HystrixEventStreamServlet
name|hystrixServlet
parameter_list|()
block|{
return|return
operator|new
name|HystrixEventStreamServlet
argument_list|()
return|;
block|}
annotation|@
name|Bean
DECL|method|servletRegistrationBean ()
specifier|public
name|ServletRegistrationBean
name|servletRegistrationBean
parameter_list|()
block|{
return|return
operator|new
name|ServletRegistrationBean
argument_list|(
operator|new
name|HystrixEventStreamServlet
argument_list|()
argument_list|,
literal|"/hystrix.stream"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

