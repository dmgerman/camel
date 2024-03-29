begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
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
name|spi
operator|.
name|HeadersMapFactory
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
name|util
operator|.
name|CaseInsensitiveMap
import|;
end_import

begin_comment
comment|/**  * Default {@link HeadersMapFactory} which uses the {@link org.apache.camel.util.CaseInsensitiveMap CaseInsensitiveMap}.  * This implementation uses a {@link org.apache.camel.util.CaseInsensitiveMap} storing the headers.  * This allows us to be able to lookup headers using case insensitive keys, making it easier for end users  * as they do not have to be worried about using exact keys.  * See more details at {@link org.apache.camel.util.CaseInsensitiveMap}.  */
end_comment

begin_class
DECL|class|DefaultHeadersMapFactory
specifier|public
class|class
name|DefaultHeadersMapFactory
implements|implements
name|HeadersMapFactory
block|{
annotation|@
name|Override
DECL|method|newMap ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newMap
parameter_list|()
block|{
return|return
operator|new
name|CaseInsensitiveMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|newMap (Map<String, Object> map)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|CaseInsensitiveMap
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isInstanceOf (Map<String, Object> map)
specifier|public
name|boolean
name|isInstanceOf
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|map
operator|instanceof
name|CaseInsensitiveMap
return|;
block|}
annotation|@
name|Override
DECL|method|isCaseInsensitive ()
specifier|public
name|boolean
name|isCaseInsensitive
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

