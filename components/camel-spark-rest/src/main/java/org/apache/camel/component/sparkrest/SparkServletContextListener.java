begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextListener
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
name|spark
operator|.
name|Access
import|;
end_import

begin_comment
comment|/**  * A {@link javax.servlet.ServletContextListener} to ensure we initialize Spark in servlet mode.  */
end_comment

begin_class
DECL|class|SparkServletContextListener
specifier|public
class|class
name|SparkServletContextListener
implements|implements
name|ServletContextListener
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
name|ServletSparkApplication
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|contextInitialized (ServletContextEvent event)
specifier|public
name|void
name|contextInitialized
parameter_list|(
name|ServletContextEvent
name|event
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"contextInitialized"
argument_list|)
expr_stmt|;
comment|// force spark to be in Servlet mode
name|Access
operator|.
name|runFromServlet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|contextDestroyed (ServletContextEvent event)
specifier|public
name|void
name|contextDestroyed
parameter_list|(
name|ServletContextEvent
name|event
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"contextDestroyed"
argument_list|)
expr_stmt|;
comment|// noop
block|}
block|}
end_class

end_unit

