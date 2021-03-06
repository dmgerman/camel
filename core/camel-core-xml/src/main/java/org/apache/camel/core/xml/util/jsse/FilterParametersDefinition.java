begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.core.xml.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|core
operator|.
name|xml
operator|.
name|util
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
import|;
end_import

begin_comment
comment|/**  * Represents a set of regular expression based filter patterns for  * including and excluding content of some type.  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"filterParameters"
argument_list|,
name|propOrder
operator|=
block|{
literal|"include"
block|,
literal|"exclude"
block|}
argument_list|)
DECL|class|FilterParametersDefinition
specifier|public
class|class
name|FilterParametersDefinition
block|{
DECL|field|include
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|include
decl_stmt|;
DECL|field|exclude
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|exclude
decl_stmt|;
comment|/**      * Returns a live copy of the list of patterns to include.      * The list of excludes takes precedence over the include patterns.      *      * @return the list of patterns to include      */
DECL|method|getInclude ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getInclude
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|include
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|include
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|include
return|;
block|}
comment|/**      * Returns a live copy of the list of patterns to exclude.      * This list takes precedence over the include patterns.      *      * @return the list of patterns to exclude      */
DECL|method|getExclude ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExclude
parameter_list|()
block|{
if|if
condition|(
name|exclude
operator|==
literal|null
condition|)
block|{
name|exclude
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|exclude
return|;
block|}
block|}
end_class

end_unit

