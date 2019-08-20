begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|type
operator|.
name|TypeReference
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|Limits
operator|.
name|Usage
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RecentItem
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RestError
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SearchResult
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|Version
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|analytics
operator|.
name|reports
operator|.
name|RecentReport
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|analytics
operator|.
name|reports
operator|.
name|ReportInstance
import|;
end_import

begin_comment
comment|/**  * Class that holds {@link TypeReference} instances needed for Jackson mapper to  * support generics.  */
end_comment

begin_class
DECL|class|TypeReferences
specifier|public
specifier|final
class|class
name|TypeReferences
block|{
DECL|field|USAGES_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Usage
argument_list|>
argument_list|>
name|USAGES_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Usage
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|REST_ERROR_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RestError
argument_list|>
argument_list|>
name|REST_ERROR_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RestError
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|REPORT_INSTANCE_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|ReportInstance
argument_list|>
argument_list|>
name|REPORT_INSTANCE_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|ReportInstance
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|RECENT_REPORT_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RecentReport
argument_list|>
argument_list|>
name|RECENT_REPORT_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RecentReport
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|STRING_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|STRING_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|VERSION_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|Version
argument_list|>
argument_list|>
name|VERSION_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|Version
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|SEARCH_RESULT_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|SearchResult
argument_list|>
argument_list|>
name|SEARCH_RESULT_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|SearchResult
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|field|RECENT_ITEM_LIST_TYPE
specifier|public
specifier|static
specifier|final
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RecentItem
argument_list|>
argument_list|>
name|RECENT_ITEM_LIST_TYPE
init|=
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RecentItem
argument_list|>
argument_list|>
argument_list|()
block|{     }
decl_stmt|;
DECL|method|TypeReferences ()
specifier|private
name|TypeReferences
parameter_list|()
block|{
comment|// not meant for instantiation, only for TypeReference constants
block|}
block|}
end_class

end_unit

