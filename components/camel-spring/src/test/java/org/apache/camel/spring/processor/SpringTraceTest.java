begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
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
name|ProducerTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit38
operator|.
name|AbstractJUnit38SpringContextTests
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|SpringTraceTest
specifier|public
class|class
name|SpringTraceTest
extends|extends
name|AbstractJUnit38SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|camelTemplate
specifier|protected
name|ProducerTemplate
name|camelTemplate
decl_stmt|;
DECL|method|testTracing ()
specifier|public
name|void
name|testTracing
parameter_list|()
throws|throws
name|Exception
block|{
name|camelTemplate
operator|.
name|sendBody
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|camelTemplate
operator|.
name|sendBody
argument_list|(
literal|1234
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

