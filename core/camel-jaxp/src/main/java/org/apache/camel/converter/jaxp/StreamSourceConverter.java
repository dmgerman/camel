begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
package|;
end_package

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
name|Reader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Converter
import|;
end_import

begin_comment
comment|/**  * A converter from {@link StreamSource} objects  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|StreamSourceConverter
specifier|public
specifier|final
class|class
name|StreamSourceConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|StreamSourceConverter ()
specifier|private
name|StreamSourceConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toInputStream (StreamSource source)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|StreamSource
name|source
parameter_list|)
block|{
return|return
name|source
operator|.
name|getInputStream
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (StreamSource source)
specifier|public
specifier|static
name|Reader
name|toReader
parameter_list|(
name|StreamSource
name|source
parameter_list|)
block|{
return|return
name|source
operator|.
name|getReader
argument_list|()
return|;
block|}
block|}
end_class

end_unit

