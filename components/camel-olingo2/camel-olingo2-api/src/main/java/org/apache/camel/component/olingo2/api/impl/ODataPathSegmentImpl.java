begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.olingo2.api.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|olingo2
operator|.
name|api
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
name|Collections
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|odata2
operator|.
name|api
operator|.
name|uri
operator|.
name|PathSegment
import|;
end_import

begin_comment
comment|/**  * Copied from Olingo2 library, since URI parsing wasn't made a part of it's  * public API.  */
end_comment

begin_class
DECL|class|ODataPathSegmentImpl
specifier|public
class|class
name|ODataPathSegmentImpl
implements|implements
name|PathSegment
block|{
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
DECL|field|matrixParameter
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|matrixParameter
decl_stmt|;
DECL|method|ODataPathSegmentImpl (final String path, final Map<String, List<String>> matrixParameters)
specifier|public
name|ODataPathSegmentImpl
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|matrixParameters
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|unmodifiableMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|matrixParameters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|matrixParameters
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|matrixParameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|unmodifiableMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
block|}
name|matrixParameter
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|unmodifiableMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
annotation|@
name|Override
DECL|method|getMatrixParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getMatrixParameters
parameter_list|()
block|{
return|return
name|matrixParameter
return|;
block|}
block|}
end_class

end_unit

