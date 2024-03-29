begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.perf.esb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|perf
operator|.
name|esb
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
name|util
operator|.
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|XsltEsbPerformanceIntegrationTest
specifier|public
class|class
name|XsltEsbPerformanceIntegrationTest
extends|extends
name|AbstractBaseEsbPerformanceIntegrationTest
block|{
annotation|@
name|Test
DECL|method|testXSLTProxy ()
specifier|public
name|void
name|testXSLTProxy
parameter_list|()
throws|throws
name|Exception
block|{
comment|// warm up with 1.000 messages so that the JIT compiler kicks in
name|send
argument_list|(
literal|"http://127.0.0.1:8192/service/XSLTProxy"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|send
argument_list|(
literal|"http://127.0.0.1:8192/service/XSLTProxy"
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ran {} tests in {}ms"
argument_list|,
name|count
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"OSGI-INF/blueprint/xslt-bundle-context.xml"
return|;
block|}
block|}
end_class

end_unit

