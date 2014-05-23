begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * Base class for a collection of ApiMethods. Meant to be extended by Components to create the api name map.  */
end_comment

begin_class
DECL|class|ApiCollection
specifier|public
specifier|abstract
class|class
name|ApiCollection
block|{
DECL|field|apis
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ApiMethodHelper
argument_list|>
name|apis
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ApiMethodHelper
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getHelper (String apiName)
specifier|public
specifier|final
name|ApiMethodHelper
name|getHelper
parameter_list|(
name|String
name|apiName
parameter_list|)
block|{
return|return
name|apis
operator|.
name|get
argument_list|(
name|apiName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

