begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.car
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
name|model
operator|.
name|car
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|";"
argument_list|,
name|skipFirstLine
operator|=
literal|true
argument_list|,
name|quoting
operator|=
literal|true
argument_list|,
name|crlf
operator|=
literal|"UNIX"
argument_list|)
DECL|class|Car
specifier|public
class|class
name|Car
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|stockid
specifier|private
name|String
name|stockid
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|)
DECL|field|make
specifier|private
name|String
name|make
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|3
argument_list|)
DECL|field|model
specifier|private
name|String
name|model
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|4
argument_list|)
DECL|field|deriv
specifier|private
name|String
name|deriv
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|5
argument_list|)
DECL|field|series
specifier|private
name|String
name|series
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|6
argument_list|)
DECL|field|registration
specifier|private
name|String
name|registration
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|7
argument_list|)
DECL|field|chassis
specifier|private
name|String
name|chassis
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|8
argument_list|)
DECL|field|engine
specifier|private
name|String
name|engine
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|9
argument_list|)
DECL|field|year
specifier|private
name|int
name|year
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|10
argument_list|,
name|precision
operator|=
literal|1
argument_list|)
DECL|field|klms
specifier|private
name|double
name|klms
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|11
argument_list|)
DECL|field|body
specifier|private
name|String
name|body
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|12
argument_list|)
DECL|field|colour
specifier|private
name|Colour
name|colour
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|13
argument_list|)
DECL|field|enginesize
specifier|private
name|String
name|enginesize
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|14
argument_list|)
DECL|field|trans
specifier|private
name|String
name|trans
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|15
argument_list|)
DECL|field|fuel
specifier|private
name|String
name|fuel
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|16
argument_list|)
DECL|field|options
specifier|private
name|String
name|options
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|17
argument_list|)
DECL|field|desc
specifier|private
name|String
name|desc
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|18
argument_list|)
DECL|field|status
specifier|private
name|String
name|status
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|19
argument_list|,
name|precision
operator|=
literal|1
argument_list|)
DECL|field|price
specifier|private
name|double
name|price
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|20
argument_list|)
DECL|field|nvic
specifier|private
name|String
name|nvic
decl_stmt|;
DECL|method|getStockid ()
specifier|public
name|String
name|getStockid
parameter_list|()
block|{
return|return
name|stockid
return|;
block|}
DECL|method|setStockid (String stockid)
specifier|public
name|void
name|setStockid
parameter_list|(
name|String
name|stockid
parameter_list|)
block|{
name|this
operator|.
name|stockid
operator|=
name|stockid
expr_stmt|;
block|}
DECL|method|getMake ()
specifier|public
name|String
name|getMake
parameter_list|()
block|{
return|return
name|make
return|;
block|}
DECL|method|setMake (String make)
specifier|public
name|void
name|setMake
parameter_list|(
name|String
name|make
parameter_list|)
block|{
name|this
operator|.
name|make
operator|=
name|make
expr_stmt|;
block|}
DECL|method|getModel ()
specifier|public
name|String
name|getModel
parameter_list|()
block|{
return|return
name|model
return|;
block|}
DECL|method|setModel (String model)
specifier|public
name|void
name|setModel
parameter_list|(
name|String
name|model
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
name|model
expr_stmt|;
block|}
DECL|method|getDeriv ()
specifier|public
name|String
name|getDeriv
parameter_list|()
block|{
return|return
name|deriv
return|;
block|}
DECL|method|setDeriv (String deriv)
specifier|public
name|void
name|setDeriv
parameter_list|(
name|String
name|deriv
parameter_list|)
block|{
name|this
operator|.
name|deriv
operator|=
name|deriv
expr_stmt|;
block|}
DECL|method|getSeries ()
specifier|public
name|String
name|getSeries
parameter_list|()
block|{
return|return
name|series
return|;
block|}
DECL|method|setSeries (String series)
specifier|public
name|void
name|setSeries
parameter_list|(
name|String
name|series
parameter_list|)
block|{
name|this
operator|.
name|series
operator|=
name|series
expr_stmt|;
block|}
DECL|method|getRegistration ()
specifier|public
name|String
name|getRegistration
parameter_list|()
block|{
return|return
name|registration
return|;
block|}
DECL|method|setRegistration (String registration)
specifier|public
name|void
name|setRegistration
parameter_list|(
name|String
name|registration
parameter_list|)
block|{
name|this
operator|.
name|registration
operator|=
name|registration
expr_stmt|;
block|}
DECL|method|getChassis ()
specifier|public
name|String
name|getChassis
parameter_list|()
block|{
return|return
name|chassis
return|;
block|}
DECL|method|setChassis (String chassis)
specifier|public
name|void
name|setChassis
parameter_list|(
name|String
name|chassis
parameter_list|)
block|{
name|this
operator|.
name|chassis
operator|=
name|chassis
expr_stmt|;
block|}
DECL|method|getEngine ()
specifier|public
name|String
name|getEngine
parameter_list|()
block|{
return|return
name|engine
return|;
block|}
DECL|method|setEngine (String engine)
specifier|public
name|void
name|setEngine
parameter_list|(
name|String
name|engine
parameter_list|)
block|{
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
block|}
DECL|method|getYear ()
specifier|public
name|int
name|getYear
parameter_list|()
block|{
return|return
name|year
return|;
block|}
DECL|method|setYear (int year)
specifier|public
name|void
name|setYear
parameter_list|(
name|int
name|year
parameter_list|)
block|{
name|this
operator|.
name|year
operator|=
name|year
expr_stmt|;
block|}
DECL|method|getKlms ()
specifier|public
name|double
name|getKlms
parameter_list|()
block|{
return|return
name|klms
return|;
block|}
DECL|method|setKlms (double klms)
specifier|public
name|void
name|setKlms
parameter_list|(
name|double
name|klms
parameter_list|)
block|{
name|this
operator|.
name|klms
operator|=
name|klms
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|public
name|String
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (String body)
specifier|public
name|void
name|setBody
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
DECL|method|getColour ()
specifier|public
name|Colour
name|getColour
parameter_list|()
block|{
return|return
name|colour
return|;
block|}
DECL|method|setColour (Colour colour)
specifier|public
name|void
name|setColour
parameter_list|(
name|Colour
name|colour
parameter_list|)
block|{
name|this
operator|.
name|colour
operator|=
name|colour
expr_stmt|;
block|}
DECL|method|getEnginesize ()
specifier|public
name|String
name|getEnginesize
parameter_list|()
block|{
return|return
name|enginesize
return|;
block|}
DECL|method|setEnginesize (String enginesize)
specifier|public
name|void
name|setEnginesize
parameter_list|(
name|String
name|enginesize
parameter_list|)
block|{
name|this
operator|.
name|enginesize
operator|=
name|enginesize
expr_stmt|;
block|}
DECL|method|getTrans ()
specifier|public
name|String
name|getTrans
parameter_list|()
block|{
return|return
name|trans
return|;
block|}
DECL|method|setTrans (String trans)
specifier|public
name|void
name|setTrans
parameter_list|(
name|String
name|trans
parameter_list|)
block|{
name|this
operator|.
name|trans
operator|=
name|trans
expr_stmt|;
block|}
DECL|method|getFuel ()
specifier|public
name|String
name|getFuel
parameter_list|()
block|{
return|return
name|fuel
return|;
block|}
DECL|method|setFuel (String fuel)
specifier|public
name|void
name|setFuel
parameter_list|(
name|String
name|fuel
parameter_list|)
block|{
name|this
operator|.
name|fuel
operator|=
name|fuel
expr_stmt|;
block|}
DECL|method|getOptions ()
specifier|public
name|String
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
DECL|method|setOptions (String options)
specifier|public
name|void
name|setOptions
parameter_list|(
name|String
name|options
parameter_list|)
block|{
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
DECL|method|getDesc ()
specifier|public
name|String
name|getDesc
parameter_list|()
block|{
return|return
name|desc
return|;
block|}
DECL|method|setDesc (String desc)
specifier|public
name|void
name|setDesc
parameter_list|(
name|String
name|desc
parameter_list|)
block|{
name|this
operator|.
name|desc
operator|=
name|desc
expr_stmt|;
block|}
DECL|method|getStatus ()
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|setStatus (String status)
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getPrice ()
specifier|public
name|double
name|getPrice
parameter_list|()
block|{
return|return
name|price
return|;
block|}
DECL|method|setPrice (double price)
specifier|public
name|void
name|setPrice
parameter_list|(
name|double
name|price
parameter_list|)
block|{
name|this
operator|.
name|price
operator|=
name|price
expr_stmt|;
block|}
DECL|method|getNvic ()
specifier|public
name|String
name|getNvic
parameter_list|()
block|{
return|return
name|nvic
return|;
block|}
DECL|method|setNvic (String nvic)
specifier|public
name|void
name|setNvic
parameter_list|(
name|String
name|nvic
parameter_list|)
block|{
name|this
operator|.
name|nvic
operator|=
name|nvic
expr_stmt|;
block|}
DECL|enum|Colour
specifier|public
enum|enum
name|Colour
block|{
DECL|enumConstant|BLACK
name|BLACK
block|}
block|}
end_class

end_unit

