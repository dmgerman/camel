begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|UuidGenerator
import|;
end_import

begin_comment
comment|/**  * This implementation uses a counter which increments by one.  * This generator is not unique per host or JVM, as its private per CamelContext.  */
end_comment

begin_class
DECL|class|SimpleUuidGenerator
specifier|public
class|class
name|SimpleUuidGenerator
implements|implements
name|UuidGenerator
block|{
DECL|field|id
specifier|private
specifier|final
name|AtomicLong
name|id
init|=
operator|new
name|AtomicLong
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|generateUuid ()
specifier|public
name|String
name|generateUuid
parameter_list|()
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|id
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

