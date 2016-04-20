begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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
name|CamelContext
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
name|ApplicationContext
import|;
end_import

begin_comment
comment|/**  * Controller to keep the main running and perform graceful shutdown when the JVM is stopped.  */
end_comment

begin_class
DECL|class|CamelMainRunController
specifier|public
class|class
name|CamelMainRunController
block|{
DECL|field|controller
specifier|private
specifier|final
name|CamelSpringBootApplicationController
name|controller
decl_stmt|;
DECL|field|daemon
specifier|private
specifier|final
name|Thread
name|daemon
decl_stmt|;
DECL|method|CamelMainRunController (ApplicationContext applicationContext, CamelContext camelContext)
specifier|public
name|CamelMainRunController
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|controller
operator|=
operator|new
name|CamelSpringBootApplicationController
argument_list|(
name|applicationContext
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|daemon
operator|=
operator|new
name|Thread
argument_list|(
operator|new
name|DaemonTask
argument_list|()
argument_list|,
literal|"CamelMainRunController"
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|daemon
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|class|DaemonTask
specifier|private
specifier|final
class|class
name|DaemonTask
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|controller
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

