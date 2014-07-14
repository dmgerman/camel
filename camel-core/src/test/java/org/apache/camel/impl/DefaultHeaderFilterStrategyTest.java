begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultHeaderFilterStrategyTest
specifier|public
class|class
name|DefaultHeaderFilterStrategyTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSimpleDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testSimpleDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setAllowNullValues
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|comp
operator|.
name|isAllowNullValues
argument_list|()
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setLowerCase
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|comp
operator|.
name|isLowerCase
argument_list|()
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setCaseInsensitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|comp
operator|.
name|isCaseInsensitive
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInFilterDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testInFilterDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setInFilter
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
argument_list|,
name|comp
operator|.
name|getInFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInFilterDoFilterDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testInFilterDoFilterDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setInFilter
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|comp
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|comp
operator|.
name|applyFilterToExternalHeaders
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOutFilterDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testOutFilterDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setOutFilter
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|set
argument_list|,
name|comp
operator|.
name|getOutFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOutFilterDoFilterDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testOutFilterDoFilterDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setOutFilter
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|comp
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"bar"
argument_list|,
literal|123
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|comp
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"foo"
argument_list|,
literal|"cheese"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCaseInsensitiveHeaderNameDoFilterDefaultHeaderFilterStrategy ()
specifier|public
name|void
name|testCaseInsensitiveHeaderNameDoFilterDefaultHeaderFilterStrategy
parameter_list|()
block|{
name|DefaultHeaderFilterStrategy
name|comp
init|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
decl_stmt|;
name|comp
operator|.
name|setCaseInsensitive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"Content-Type"
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setOutFilter
argument_list|(
name|set
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"content-type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|comp
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"content-type"
argument_list|,
literal|"application/xml"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|comp
operator|.
name|applyFilterToCamelHeaders
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/json"
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

