begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.main
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|main
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
DECL|class|MainExample
specifier|public
class|class
name|MainExample
block|{
DECL|field|main
specifier|private
name|Main
name|main
decl_stmt|;
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
throws|throws
name|Exception
block|{
name|MainExample
name|example
init|=
operator|new
name|MainExample
argument_list|()
decl_stmt|;
name|example
operator|.
name|boot
argument_list|()
expr_stmt|;
block|}
DECL|method|boot ()
specifier|public
name|void
name|boot
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create a Main instance
name|main
operator|=
operator|new
name|Main
argument_list|()
expr_stmt|;
comment|// enable hangup support so you can press ctrl + c to terminate the JVM
name|main
operator|.
name|enableHangupSupport
argument_list|()
expr_stmt|;
comment|// bind MyBean into the registery
name|main
operator|.
name|bind
argument_list|(
literal|"foo"
argument_list|,
operator|new
name|MyBean
argument_list|()
argument_list|)
expr_stmt|;
comment|// add routes
name|main
operator|.
name|addRouteBuilder
argument_list|(
operator|new
name|MyRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
comment|// run until you terminate the JVM
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Starting Camel. Use ctrl + c to terminate the JVM.\n"
argument_list|)
expr_stmt|;
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
DECL|class|MyRouteBuilder
specifier|private
specifier|static
class|class
name|MyRouteBuilder
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
name|from
argument_list|(
literal|"timer:foo?delay=2000"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoked timer at "
operator|+
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|beanRef
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MyBean
specifier|public
specifier|static
class|class
name|MyBean
block|{
DECL|method|callMe ()
specifier|public
name|void
name|callMe
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"MyBean.calleMe method has been called"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

