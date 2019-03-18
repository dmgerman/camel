begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|util
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
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|AS2CompressionAlgorithm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSCompressedDataGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OutputCompressor
import|;
end_import

begin_class
DECL|class|CompressionUtils
specifier|public
specifier|final
class|class
name|CompressionUtils
block|{
DECL|method|CompressionUtils ()
specifier|private
name|CompressionUtils
parameter_list|()
block|{     }
DECL|method|createCompressedDataGenerator ()
specifier|public
specifier|static
name|CMSCompressedDataGenerator
name|createCompressedDataGenerator
parameter_list|()
block|{
return|return
operator|new
name|CMSCompressedDataGenerator
argument_list|()
return|;
block|}
DECL|method|createCompressor (AS2CompressionAlgorithm compressionAlgorithm)
specifier|public
specifier|static
name|OutputCompressor
name|createCompressor
parameter_list|(
name|AS2CompressionAlgorithm
name|compressionAlgorithm
parameter_list|)
block|{
return|return
name|compressionAlgorithm
operator|.
name|getOutputCompressor
argument_list|()
return|;
block|}
block|}
end_class

end_unit

