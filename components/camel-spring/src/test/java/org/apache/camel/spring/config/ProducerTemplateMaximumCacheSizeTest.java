begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|ProducerTemplate
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
name|spring
operator|.
name|SpringRunWithTestSupport
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|ProducerTemplateMaximumCacheSizeTest
specifier|public
class|class
name|ProducerTemplateMaximumCacheSizeTest
extends|extends
name|SpringRunWithTestSupport
block|{
annotation|@
name|Autowired
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testTemplateMaximumCache ()
specifier|public
name|void
name|testTemplateMaximumCache
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"Should have injected a producer template"
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|lookup
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"template"
argument_list|,
name|ProducerTemplate
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should lookup producer template"
argument_list|,
name|lookup
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|template
operator|.
name|getMaximumCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that we cache at most 50 producers to avoid it eating to much memory
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|53
condition|;
name|i
operator|++
control|)
block|{
name|Endpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:queue:"
operator|+
name|i
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|e
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
block|}
comment|// the eviction is async so force cleanup
name|template
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 50"
argument_list|,
literal|50
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// should be 0
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|template
operator|.
name|getCurrentCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

