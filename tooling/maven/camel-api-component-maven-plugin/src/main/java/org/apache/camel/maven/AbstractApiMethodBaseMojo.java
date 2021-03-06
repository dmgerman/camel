begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_comment
comment|/**  * Base class to share API method generator properties with @{link ApiComponentGeneratorMojo}.  */
end_comment

begin_class
DECL|class|AbstractApiMethodBaseMojo
specifier|public
specifier|abstract
class|class
name|AbstractApiMethodBaseMojo
extends|extends
name|AbstractSourceGeneratorMojo
block|{
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"substitutions"
argument_list|)
DECL|field|substitutions
specifier|protected
name|Substitution
index|[]
name|substitutions
init|=
operator|new
name|Substitution
index|[
literal|0
index|]
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"excludeConfigNames"
argument_list|)
DECL|field|excludeConfigNames
specifier|protected
name|String
name|excludeConfigNames
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"excludeConfigTypes"
argument_list|)
DECL|field|excludeConfigTypes
specifier|protected
name|String
name|excludeConfigTypes
decl_stmt|;
annotation|@
name|Parameter
DECL|field|extraOptions
specifier|protected
name|ExtraOption
index|[]
name|extraOptions
decl_stmt|;
block|}
end_class

end_unit

