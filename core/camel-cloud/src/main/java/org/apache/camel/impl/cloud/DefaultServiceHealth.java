begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cloud
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
name|cloud
operator|.
name|ServiceHealth
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
name|CollectionHelper
import|;
end_import

begin_class
DECL|class|DefaultServiceHealth
specifier|public
class|class
name|DefaultServiceHealth
implements|implements
name|ServiceHealth
block|{
DECL|field|healthy
specifier|private
specifier|final
name|boolean
name|healthy
decl_stmt|;
DECL|field|meta
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
decl_stmt|;
DECL|method|DefaultServiceHealth ()
specifier|public
name|DefaultServiceHealth
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultServiceHealth (boolean healthy)
specifier|public
name|DefaultServiceHealth
parameter_list|(
name|boolean
name|healthy
parameter_list|)
block|{
name|this
argument_list|(
name|healthy
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultServiceHealth (Map<String, String> meta)
specifier|public
name|DefaultServiceHealth
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
parameter_list|)
block|{
name|this
argument_list|(
literal|true
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultServiceHealth (boolean healthy, Map<String, String> meta)
specifier|public
name|DefaultServiceHealth
parameter_list|(
name|boolean
name|healthy
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|meta
parameter_list|)
block|{
name|this
operator|.
name|healthy
operator|=
name|healthy
expr_stmt|;
name|this
operator|.
name|meta
operator|=
name|CollectionHelper
operator|.
name|unmodifiableMap
argument_list|(
name|meta
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isHealthy ()
specifier|public
name|boolean
name|isHealthy
parameter_list|()
block|{
return|return
name|this
operator|.
name|healthy
return|;
block|}
annotation|@
name|Override
DECL|method|getMetadata ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMetadata
parameter_list|()
block|{
return|return
name|this
operator|.
name|meta
return|;
block|}
block|}
end_class

end_unit

