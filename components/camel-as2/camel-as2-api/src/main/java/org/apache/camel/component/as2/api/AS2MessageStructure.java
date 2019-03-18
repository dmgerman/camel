begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api
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
package|;
end_package

begin_enum
DECL|enum|AS2MessageStructure
specifier|public
enum|enum
name|AS2MessageStructure
block|{
DECL|enumConstant|PLAIN
name|PLAIN
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|SIGNED
name|SIGNED
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|ENCRYPTED
name|ENCRYPTED
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|SIGNED_ENCRYPTED
name|SIGNED_ENCRYPTED
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
block|,
DECL|enumConstant|PLAIN_COMPRESSED
name|PLAIN_COMPRESSED
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|SIGNED_COMPRESSED
name|SIGNED_COMPRESSED
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|ENCRYPTED_COMPRESSED
name|ENCRYPTED_COMPRESSED
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|ENCRYPTED_COMPRESSED_SIGNED
name|ENCRYPTED_COMPRESSED_SIGNED
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
block|;
DECL|field|isSigned
specifier|private
specifier|final
name|boolean
name|isSigned
decl_stmt|;
DECL|field|isEncrypted
specifier|private
specifier|final
name|boolean
name|isEncrypted
decl_stmt|;
DECL|field|isCompressed
specifier|private
specifier|final
name|boolean
name|isCompressed
decl_stmt|;
DECL|method|AS2MessageStructure (boolean isSigned, boolean isEncrypted, boolean isCompressed)
specifier|private
name|AS2MessageStructure
parameter_list|(
name|boolean
name|isSigned
parameter_list|,
name|boolean
name|isEncrypted
parameter_list|,
name|boolean
name|isCompressed
parameter_list|)
block|{
name|this
operator|.
name|isSigned
operator|=
name|isSigned
expr_stmt|;
name|this
operator|.
name|isEncrypted
operator|=
name|isEncrypted
expr_stmt|;
name|this
operator|.
name|isCompressed
operator|=
name|isCompressed
expr_stmt|;
block|}
DECL|method|isSigned ()
specifier|public
name|boolean
name|isSigned
parameter_list|()
block|{
return|return
name|isSigned
return|;
block|}
DECL|method|isEncrypted ()
specifier|public
name|boolean
name|isEncrypted
parameter_list|()
block|{
return|return
name|isEncrypted
return|;
block|}
DECL|method|isCompressed ()
specifier|public
name|boolean
name|isCompressed
parameter_list|()
block|{
return|return
name|isCompressed
return|;
block|}
block|}
end_enum

end_unit

