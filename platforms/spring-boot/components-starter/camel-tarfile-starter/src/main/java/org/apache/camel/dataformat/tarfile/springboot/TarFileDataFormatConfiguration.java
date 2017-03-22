begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tarfile.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tarfile
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel Tar file support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.tarfile"
argument_list|)
DECL|class|TarFileDataFormatConfiguration
specifier|public
class|class
name|TarFileDataFormatConfiguration
block|{
comment|/**      * If the tar file has more then one entry the setting this option to true      * allows to work with the splitter EIP to split the data using an iterator      * in a streaming mode.      */
DECL|field|usingIterator
specifier|private
name|Boolean
name|usingIterator
init|=
literal|false
decl_stmt|;
comment|/**      * If the tar file has more then one entry setting this option to true      * allows to get the iterator even if the directory is empty      */
DECL|field|allowEmptyDirectory
specifier|private
name|Boolean
name|allowEmptyDirectory
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getUsingIterator ()
specifier|public
name|Boolean
name|getUsingIterator
parameter_list|()
block|{
return|return
name|usingIterator
return|;
block|}
DECL|method|setUsingIterator (Boolean usingIterator)
specifier|public
name|void
name|setUsingIterator
parameter_list|(
name|Boolean
name|usingIterator
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
name|usingIterator
expr_stmt|;
block|}
DECL|method|getAllowEmptyDirectory ()
specifier|public
name|Boolean
name|getAllowEmptyDirectory
parameter_list|()
block|{
return|return
name|allowEmptyDirectory
return|;
block|}
DECL|method|setAllowEmptyDirectory (Boolean allowEmptyDirectory)
specifier|public
name|void
name|setAllowEmptyDirectory
parameter_list|(
name|Boolean
name|allowEmptyDirectory
parameter_list|)
block|{
name|this
operator|.
name|allowEmptyDirectory
operator|=
name|allowEmptyDirectory
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

