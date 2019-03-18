begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * A map that uses case insensitive keys, but preserves the original key cases.  *<p/>  * The map is based on {@link TreeMap} and therefore uses O(n) for lookup and not O(1) as a {@link java.util.HashMap} does.  *<p/>  * This map is<b>not</b> designed to be thread safe as concurrent access to it is not supposed to be performed  * by the Camel routing engine.  */
end_comment

begin_class
DECL|class|CaseInsensitiveMap
specifier|public
class|class
name|CaseInsensitiveMap
extends|extends
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8538318195477618308L
decl_stmt|;
DECL|method|CaseInsensitiveMap ()
specifier|public
name|CaseInsensitiveMap
parameter_list|()
block|{
name|super
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
block|}
DECL|method|CaseInsensitiveMap (Map<? extends String, ?> map)
specifier|public
name|CaseInsensitiveMap
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
comment|// must use the insensitive order
name|super
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

