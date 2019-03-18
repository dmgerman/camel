begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|idempotent
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
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cache
operator|.
name|support
operator|.
name|AbstractCacheManager
import|;
end_import

begin_class
DECL|class|MySpringIdempotentCacheManager
specifier|public
class|class
name|MySpringIdempotentCacheManager
extends|extends
name|AbstractCacheManager
block|{
DECL|field|cache
specifier|private
specifier|final
name|Collection
argument_list|<
name|Cache
argument_list|>
name|cache
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|MySpringIdempotentCacheManager ()
specifier|public
name|MySpringIdempotentCacheManager
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadCaches ()
specifier|protected
name|Collection
argument_list|<
name|?
extends|extends
name|Cache
argument_list|>
name|loadCaches
parameter_list|()
block|{
name|cache
operator|.
name|add
argument_list|(
operator|new
name|MyCache
argument_list|(
literal|"idempotent"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cache
return|;
block|}
block|}
end_class

end_unit

