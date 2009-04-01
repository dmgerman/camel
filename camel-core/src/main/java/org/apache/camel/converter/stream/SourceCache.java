begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
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
name|StreamCache
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
name|converter
operator|.
name|jaxp
operator|.
name|StringSource
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.StreamCache} implementation for {@link org.apache.camel.converter.jaxp.StringSource}s  */
end_comment

begin_class
DECL|class|SourceCache
specifier|public
class|class
name|SourceCache
extends|extends
name|StringSource
implements|implements
name|StreamCache
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|SourceCache ()
specifier|public
name|SourceCache
parameter_list|()
block|{     }
DECL|method|SourceCache (String data)
specifier|public
name|SourceCache
parameter_list|(
name|String
name|data
parameter_list|)
block|{
operator|new
name|StringSource
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|// do nothing here
block|}
block|}
end_class

end_unit

