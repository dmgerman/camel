begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pdf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pdf
package|;
end_package

begin_class
DECL|class|PdfHeaderConstants
specifier|public
specifier|final
class|class
name|PdfHeaderConstants
block|{
DECL|field|PROTECTION_POLICY_HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|PROTECTION_POLICY_HEADER_NAME
init|=
literal|"protection-policy"
decl_stmt|;
DECL|field|PDF_DOCUMENT_HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|PDF_DOCUMENT_HEADER_NAME
init|=
literal|"pdf-document"
decl_stmt|;
DECL|field|DECRYPTION_MATERIAL_HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DECRYPTION_MATERIAL_HEADER_NAME
init|=
literal|"decryption-material"
decl_stmt|;
DECL|method|PdfHeaderConstants ()
specifier|private
name|PdfHeaderConstants
parameter_list|()
block|{ }
block|}
end_class

end_unit

