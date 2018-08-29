begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SedaDefaultUnboundedQueueSizeTest
specifier|public
class|class
name|SedaDefaultUnboundedQueueSizeTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSedaDefaultUnboundedQueueSize ()
specifier|public
name|void
name|testSedaDefaultUnboundedQueueSize
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSedaDefaultBoundedQueueSize ()
specifier|public
name|void
name|testSedaDefaultBoundedQueueSize
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo?size=500"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|500
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// sending one more hit the limit
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Message overflow"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|IllegalStateException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

