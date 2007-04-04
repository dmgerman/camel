begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|converter
operator|.
name|TypeConverterRegistry
import|;
end_import

begin_comment
comment|/**  * A caching proxy so that a single   * @version $Revision$  */
end_comment

begin_class
DECL|class|CachingInjector
specifier|public
class|class
name|CachingInjector
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|repository
specifier|private
specifier|final
name|TypeConverterRegistry
name|repository
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
DECL|field|instance
specifier|private
name|T
name|instance
decl_stmt|;
DECL|method|CachingInjector (TypeConverterRegistry repository, Class<T> type)
specifier|public
name|CachingInjector
parameter_list|(
name|TypeConverterRegistry
name|repository
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|newInstance ()
specifier|public
specifier|synchronized
name|T
name|newInstance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
name|createInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
DECL|method|createInstance (Class<T> type)
specifier|protected
name|T
name|createInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|repository
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

