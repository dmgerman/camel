begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|TypeConverter
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CamelContextLazyLoadTypeConvertersTest
specifier|public
class|class
name|CamelContextLazyLoadTypeConvertersTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
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
literal|"org/apache/camel/spring/CamelContextLazyLoadTypeConvertersTest.xml"
argument_list|)
return|;
block|}
DECL|method|testConvert ()
specifier|public
name|void
name|testConvert
parameter_list|()
throws|throws
name|Exception
block|{
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|Integer
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|"1000"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to Integer"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|String
name|text
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Converted to String"
argument_list|,
literal|"1000"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

