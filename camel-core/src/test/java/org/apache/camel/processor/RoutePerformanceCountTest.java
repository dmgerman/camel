begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|ContextTestSupport
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

begin_class
DECL|class|RoutePerformanceCountTest
specifier|public
class|class
name|RoutePerformanceCountTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|processor
specifier|private
name|CountProcessor
name|processor
init|=
operator|new
name|CountProcessor
argument_list|()
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
literal|500
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"direct:start"
decl_stmt|;
DECL|method|testSendMessages ()
specifier|public
name|void
name|testSendMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|size
argument_list|,
name|processor
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"RoutePerformanceCountTest: Sent: "
operator|+
name|size
operator|+
literal|" Took: "
operator|+
name|delta
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|canRunOnThisPlatform ()
specifier|protected
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
comment|// HP-UX is just to slow to run this test
return|return
operator|!
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"hp-ux"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:a?level=OFF"
argument_list|,
literal|"log:b?level=OFF"
argument_list|,
literal|"direct:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:c"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|process
argument_list|(
name|processor
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
name|processor
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CountProcessor
specifier|private
specifier|static
class|class
name|CountProcessor
implements|implements
name|Processor
block|{
DECL|field|counter
specifier|private
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|process (Exchange exchange)
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
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
operator|.
name|intValue
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

