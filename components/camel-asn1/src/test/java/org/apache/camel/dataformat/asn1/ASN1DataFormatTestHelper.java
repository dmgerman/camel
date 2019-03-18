begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.asn1
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|asn1
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_class
DECL|class|ASN1DataFormatTestHelper
specifier|final
class|class
name|ASN1DataFormatTestHelper
block|{
DECL|method|ASN1DataFormatTestHelper ()
specifier|private
name|ASN1DataFormatTestHelper
parameter_list|()
block|{     }
DECL|method|reteriveByteArrayInputStream (File file)
specifier|public
specifier|static
name|ByteArrayInputStream
name|reteriveByteArrayInputStream
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|FileUtils
operator|.
name|readFileToByteArray
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

