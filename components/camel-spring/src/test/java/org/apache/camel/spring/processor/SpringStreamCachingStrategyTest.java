begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spring
operator|.
name|SpringTestSupport
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
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|FileUtil
operator|.
name|normalizePath
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SpringStreamCachingStrategyTest
specifier|public
class|class
name|SpringStreamCachingStrategyTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/processor/SpringStreamCachingStrategyTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testStreamCaching ()
specifier|public
name|void
name|testStreamCaching
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|normalizePath
argument_list|(
literal|"target/cachedir"
argument_list|)
argument_list|,
name|normalizePath
argument_list|(
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolDirectory
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|4096
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|,
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getBufferSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|8192
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|,
name|context
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|getSpoolThreshold
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

