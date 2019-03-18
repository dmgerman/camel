begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.corda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|corda
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|contracts
operator|.
name|OwnableState
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|flows
operator|.
name|FlowLogic
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|Vault
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|PageSpecification
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|QueryCriteria
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|Sort
import|;
end_import

begin_import
import|import
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|SortAttribute
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import static
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|QueryCriteriaUtils
operator|.
name|DEFAULT_PAGE_NUM
import|;
end_import

begin_import
import|import static
name|net
operator|.
name|corda
operator|.
name|core
operator|.
name|node
operator|.
name|services
operator|.
name|vault
operator|.
name|QueryCriteriaUtils
operator|.
name|MAX_PAGE_SIZE
import|;
end_import

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"This integration test requires a locally running corda node such cordapp-template-java"
argument_list|)
DECL|class|CordaConsumerTestSupport
specifier|public
class|class
name|CordaConsumerTestSupport
extends|extends
name|CordaTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseAdviceWith ()
specifier|public
name|boolean
name|isUseAdviceWith
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
literal|"Hello"
block|}
decl_stmt|;
name|Class
argument_list|<
name|FlowLogic
argument_list|<
name|String
argument_list|>
argument_list|>
name|flowLociClass
init|=
operator|(
name|Class
argument_list|<
name|FlowLogic
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|Class
operator|.
name|forName
argument_list|(
literal|"org.apache.camel.component.corda.CamelFlow"
argument_list|)
decl_stmt|;
name|QueryCriteria
operator|.
name|VaultQueryCriteria
name|criteria
init|=
operator|new
name|QueryCriteria
operator|.
name|VaultQueryCriteria
argument_list|(
name|Vault
operator|.
name|StateStatus
operator|.
name|CONSUMED
argument_list|)
decl_stmt|;
name|PageSpecification
name|pageSpec
init|=
operator|new
name|PageSpecification
argument_list|(
name|DEFAULT_PAGE_NUM
argument_list|,
name|MAX_PAGE_SIZE
argument_list|)
decl_stmt|;
name|Sort
operator|.
name|SortColumn
name|sortByUid
init|=
operator|new
name|Sort
operator|.
name|SortColumn
argument_list|(
operator|new
name|SortAttribute
operator|.
name|Standard
argument_list|(
name|Sort
operator|.
name|LinearStateAttribute
operator|.
name|UUID
argument_list|)
argument_list|,
name|Sort
operator|.
name|Direction
operator|.
name|DESC
argument_list|)
decl_stmt|;
name|Sort
name|sort
init|=
operator|new
name|Sort
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|sortByUid
argument_list|)
argument_list|)
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"contractStateClass"
argument_list|,
name|OwnableState
operator|.
name|class
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"queryCriteria"
argument_list|,
name|criteria
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"pageSpecification"
argument_list|,
name|pageSpec
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"sort"
argument_list|,
name|sort
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"flowLociClass"
argument_list|,
name|flowLociClass
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"arguments"
argument_list|,
name|args
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

