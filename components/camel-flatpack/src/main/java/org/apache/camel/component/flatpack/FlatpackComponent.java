begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
package|;
end_package

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
name|camel
operator|.
name|Endpoint
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * A<a href="http://flatpack.sourceforge.net/">Flatpack Component</a>  * for working with fixed width and delimited files  */
end_comment

begin_class
DECL|class|FlatpackComponent
specifier|public
class|class
name|FlatpackComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|HEADER_ID
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_ID
init|=
literal|"header"
decl_stmt|;
DECL|field|TRAILER_ID
specifier|public
specifier|static
specifier|final
name|String
name|TRAILER_ID
init|=
literal|"trailer"
decl_stmt|;
DECL|method|FlatpackComponent ()
specifier|public
name|FlatpackComponent
parameter_list|()
block|{     }
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|fixed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"fixed:"
argument_list|)
condition|)
block|{
name|fixed
operator|=
literal|true
expr_stmt|;
name|remaining
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|"fixed:"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"delim:"
argument_list|)
condition|)
block|{
name|remaining
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|"delim:"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// lets assume the rest of the string is just a name
comment|// to differentiate different named delimited endpoints
name|remaining
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|resourceUri
init|=
name|remaining
decl_stmt|;
name|FlatpackType
name|type
init|=
name|fixed
condition|?
name|FlatpackType
operator|.
name|fixed
else|:
name|FlatpackType
operator|.
name|delim
decl_stmt|;
name|FlatpackEndpoint
name|answer
init|=
operator|new
name|FlatpackEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|resourceUri
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

