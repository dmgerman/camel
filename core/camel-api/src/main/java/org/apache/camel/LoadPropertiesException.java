begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  * Represents a failure to open a Properties file at a given URL  */
end_comment

begin_class
DECL|class|LoadPropertiesException
specifier|public
class|class
name|LoadPropertiesException
extends|extends
name|CamelException
block|{
DECL|field|url
specifier|private
specifier|final
name|URL
name|url
decl_stmt|;
DECL|method|LoadPropertiesException (URL url, Exception cause)
specifier|public
name|LoadPropertiesException
parameter_list|(
name|URL
name|url
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Failed to load URL: "
operator|+
name|url
operator|+
literal|". Reason: "
operator|+
name|cause
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

