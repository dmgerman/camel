begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_class
DECL|class|CsvUnmarshalOnCompletion
specifier|public
class|class
name|CsvUnmarshalOnCompletion
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|closeable
specifier|private
specifier|final
name|Closeable
name|closeable
decl_stmt|;
DECL|method|CsvUnmarshalOnCompletion (Closeable closeable)
specifier|public
name|CsvUnmarshalOnCompletion
parameter_list|(
name|Closeable
name|closeable
parameter_list|)
block|{
name|this
operator|.
name|closeable
operator|=
name|closeable
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDone (Exchange exchange)
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

