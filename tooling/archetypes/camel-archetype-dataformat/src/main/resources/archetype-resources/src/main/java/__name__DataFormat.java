begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|$
block|{
package|package
block|}
end_package

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|Exchange
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
name|DataFormat
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * for ${name} data.  */
end_comment

begin_class
DECL|class|$
specifier|public
class|class
name|$
block|{
name|name
block|}
end_class

begin_expr_stmt
DECL|class|$
name|DataFormat
expr|implements
name|DataFormat
block|{
specifier|public
name|void
name|marshal
argument_list|(
name|Exchange
name|exchange
argument_list|,
name|Object
name|graph
argument_list|,
name|OutputStream
name|stream
argument_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|graph
argument_list|)
block|;
name|stream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
block|;     }
specifier|public
name|Object
name|unmarshal
argument_list|(
name|Exchange
name|exchange
argument_list|,
name|InputStream
name|stream
argument_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|stream
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|bytes
return|;
end_return

unit|} }
end_unit

