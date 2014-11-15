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
name|List
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_class
DECL|class|RemovePropertiesWithExclusionTest
specifier|public
class|class
name|RemovePropertiesWithExclusionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|end
specifier|private
name|MockEndpoint
name|end
decl_stmt|;
DECL|field|mid
specifier|private
name|MockEndpoint
name|mid
decl_stmt|;
DECL|field|propertyName
specifier|private
name|String
name|propertyName
init|=
literal|"foo"
decl_stmt|;
DECL|field|expectedPropertyValue
specifier|private
name|String
name|expectedPropertyValue
init|=
literal|"bar"
decl_stmt|;
DECL|field|propertyName1
specifier|private
name|String
name|propertyName1
init|=
literal|"fee"
decl_stmt|;
DECL|field|expectedPropertyValue1
specifier|private
name|String
name|expectedPropertyValue1
init|=
literal|"bar1"
decl_stmt|;
DECL|field|propertyName2
specifier|private
name|String
name|propertyName2
init|=
literal|"fiu"
decl_stmt|;
DECL|field|expectedPropertyValue2
specifier|private
name|String
name|expectedPropertyValue2
init|=
literal|"bar2"
decl_stmt|;
DECL|field|pattern
specifier|private
name|String
name|pattern
init|=
literal|"f*"
decl_stmt|;
DECL|field|exclusion
specifier|private
name|String
name|exclusion
init|=
literal|"fiu"
decl_stmt|;
DECL|method|testSetExchangePropertiesMidRouteThenRemoveWithPatternAndExclusion ()
specifier|public
name|void
name|testSetExchangePropertiesMidRouteThenRemoveWithPatternAndExclusion
parameter_list|()
throws|throws
name|Exception
block|{
name|mid
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"message"
argument_list|)
expr_stmt|;
comment|// make sure we got the message
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|midExchanges
init|=
name|mid
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|midExchange
init|=
name|midExchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|actualPropertyValue
init|=
name|midExchange
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|actualPropertyValue1
init|=
name|midExchange
operator|.
name|getProperty
argument_list|(
name|propertyName1
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|actualPropertyValue2
init|=
name|midExchange
operator|.
name|getProperty
argument_list|(
name|propertyName2
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedPropertyValue
argument_list|,
name|actualPropertyValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPropertyValue1
argument_list|,
name|actualPropertyValue1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPropertyValue2
argument_list|,
name|actualPropertyValue2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|endExchanges
init|=
name|end
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|Exchange
name|endExchange
init|=
name|endExchanges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// property should be removed
name|assertNull
argument_list|(
name|endExchange
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endExchange
operator|.
name|getProperty
argument_list|(
name|propertyName1
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedPropertyValue2
argument_list|,
name|endExchange
operator|.
name|getProperty
argument_list|(
name|propertyName2
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|end
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|mid
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:mid"
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setProperty
argument_list|(
name|propertyName
argument_list|)
operator|.
name|constant
argument_list|(
name|expectedPropertyValue
argument_list|)
operator|.
name|setProperty
argument_list|(
name|propertyName1
argument_list|)
operator|.
name|constant
argument_list|(
name|expectedPropertyValue1
argument_list|)
operator|.
name|setProperty
argument_list|(
name|propertyName2
argument_list|)
operator|.
name|constant
argument_list|(
name|expectedPropertyValue2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:mid"
argument_list|)
operator|.
name|removeProperties
argument_list|(
name|pattern
argument_list|,
name|exclusion
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

