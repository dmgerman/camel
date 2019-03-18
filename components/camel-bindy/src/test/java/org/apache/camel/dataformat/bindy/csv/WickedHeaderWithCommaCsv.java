begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|csv
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
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
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|,
name|skipFirstLine
operator|=
literal|true
argument_list|,
name|quoting
operator|=
literal|true
argument_list|,
name|generateHeaderColumns
operator|=
literal|true
argument_list|)
DECL|class|WickedHeaderWithCommaCsv
specifier|public
class|class
name|WickedHeaderWithCommaCsv
block|{
annotation|@
name|DataField
argument_list|(
name|columnName
operator|=
literal|"Foo (one, or more, foos)"
argument_list|,
name|pos
operator|=
literal|1
argument_list|)
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|columnName
operator|=
literal|"Bar (one, or more, bars)"
argument_list|,
name|pos
operator|=
literal|2
argument_list|)
DECL|field|bar
specifier|private
name|String
name|bar
decl_stmt|;
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
DECL|method|setFoo (String foo)
specifier|public
name|void
name|setFoo
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
DECL|method|getBar ()
specifier|public
name|String
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
DECL|method|setBar (String bar)
specifier|public
name|void
name|setBar
parameter_list|(
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|WickedHeaderWithCommaCsv
name|wickedHeaderWithCommaCsv
init|=
operator|(
name|WickedHeaderWithCommaCsv
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|foo
operator|!=
literal|null
condition|?
operator|!
name|foo
operator|.
name|equals
argument_list|(
name|wickedHeaderWithCommaCsv
operator|.
name|foo
argument_list|)
else|:
name|wickedHeaderWithCommaCsv
operator|.
name|foo
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|bar
operator|!=
literal|null
condition|?
name|bar
operator|.
name|equals
argument_list|(
name|wickedHeaderWithCommaCsv
operator|.
name|bar
argument_list|)
else|:
name|wickedHeaderWithCommaCsv
operator|.
name|bar
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|foo
operator|!=
literal|null
condition|?
name|foo
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|bar
operator|!=
literal|null
condition|?
name|bar
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

